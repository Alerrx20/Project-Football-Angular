import { Injectable } from '@angular/core';
import { Observable, map, of, catchError, throwError, tap } from 'rxjs';
import { HttpClient, HttpEvent, HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import  swal  from 'sweetalert2';
import { Router } from '@angular/router';
import { Player } from './player';
import { Team } from '../teams/team';

@Injectable({
  providedIn: 'root'
})
export class PlayersService {

  private urlEndPoint: string = "http://localhost:8080/api/v1/players";
  private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient,
    private router: Router) {
  }
  
  getplayers(page: number): Observable<any> {
    return this.http.get(this.urlEndPoint + '/page/' + page).pipe(
      tap((response: any) => {
        console.log("Players Service: tap 1");
        (response.content as Player[]).forEach(player => {
          console.log(player.name);
        });
      }),
      map((response: any) => {
        (response.content as Player[]).map(player => {
          player.name = player.name.toUpperCase();
          return player;
        });
        return response;
      }),
      tap(response => {
        console.log("Players Service: tap 2");
        (response.content as Player[]).forEach(player => {
          console.log(player.name);
        });
      })
    );
  }

  create(player: Player): Observable<Player> {
    return this.http.post(this.urlEndPoint, player, {headers: this.httpHeaders}).pipe(
      map((response: any) => response.player as Player),
      catchError(e => {
        if (e.status == 400) {
          return throwError(() => e);
        }
        console.error(e.error.message);
        swal.fire(e.error.message, e.error.error, 'error');
        return throwError(() => e);
      })
    );
  }

  getPlayer(id: number): Observable<Player> {
    return this.http.get<Player>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/players']);
        console.log(e.error.message);
        swal.fire('Error ', e.error.message, 'error');
        return throwError(() => e);
      })
    );
  }

  update(player: Player): Observable<Player> {
    return this.http.put<Player>(`${this.urlEndPoint}/${player.id}`, player, {headers: this.httpHeaders}).pipe(
      map((response: any) => response.player as Player),
      catchError(e => {
        if (e.status == 400) {
          return throwError(() => e);
        }
        console.error(e.error.message);
        swal.fire(e.error.message, e.error.error, 'error');
        return throwError(() => e);
      })
    );
  }

  delete(id: number): Observable<Player> {
    return this.http.delete<Player>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.message);
        swal.fire(e.error.message, e.error.error, 'error');
        return throwError(() => e);
      })
    );
  }

  uploadPhoto(archivo: File, id: any): Observable<HttpEvent<{}>> {
    let formData = new FormData();
    formData.append("file", archivo);
    formData.append("id", id);
    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData, {
      reportProgress: true
    });
    return this.http.request(req);
  }

  getTeams(): Observable<Team[]> {
    return this.http.get<Team[]>("http://localhost:8080/api/v1/teams");
  }

}
