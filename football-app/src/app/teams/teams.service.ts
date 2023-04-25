import { Injectable } from '@angular/core';
import { Observable, map, of, catchError, throwError, tap } from 'rxjs';
import { HttpClient, HttpEvent, HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import  swal  from 'sweetalert2';
import { Router } from '@angular/router';
import { Team } from './team';

@Injectable({
  providedIn: 'root'
})
export class TeamsService {

  private urlEndPoint: string = "http://localhost:8080/api/v1/teams";
  private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient,
    private router: Router) { }

  getTeams(page: number): Observable<any> {
    return this.http.get(this.urlEndPoint + '/page/' + page).pipe(
      map((response: any) => {
        (response.content as Team[]).map(team => {
          team.name = team.name.toUpperCase();
          return team;
        });
        return response;
      })
    );
  }

  getTeam(id: number): Observable<Team> {
    return this.http.get<Team>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/teams']);
        console.log(e.error.message);
        swal.fire('Error ', e.error.message, 'error');
        return throwError(() => e);
      })
    );
  }

  create(team: Team): Observable<Team> {
    return this.http.post(this.urlEndPoint, team, {headers: this.httpHeaders}).pipe(
      map((response: any) => response.team as Team),
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

  update(team: Team): Observable<Team> {
    return this.http.put<Team>(`${this.urlEndPoint}/${team.id}`, team, {headers: this.httpHeaders}).pipe(
      map((response: any) => response.team as Team),
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

  delete(id: number): Observable<Team> {
    return this.http.delete<Team>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.message);
        swal.fire(e.error.message, e.error.error, 'error');
        return throwError(() => e);
      })
    );
  }

}
