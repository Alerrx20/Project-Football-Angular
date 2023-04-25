import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { PlayersService } from '../players.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Player } from '../player';
import { Team } from 'src/app/teams/team';
import { TeamsService } from 'src/app/teams/teams.service';

@Component({
  selector: 'app-player-form',
  templateUrl: './player-form.component.html',
  styleUrls: ['./player-form.component.css']
})
export class PlayerFormComponent {

  title = "Crear Jugador";
  player = new Player();
  teams: Team[] = [];
  errores: string [] = [];

  constructor(private playersService: PlayersService, 
    private router: Router,
     private activatedRoute: ActivatedRoute,
     private teamsService: TeamsService){}

  ngOnInit() {
    this.loadPlayer();
    this.playersService.getTeams().subscribe(teams => this.teams = teams);
  }

  loadPlayer() {
    this.activatedRoute.params.subscribe(params  => {
      let id = params['id'];
      if (id) {
        this.playersService.getPlayer(id).subscribe((player) => this.player = player);
      }
    });
  }

  create() {
    this.playersService.create(this.player).subscribe({
        next: (player) => {
          this.router.navigate(["/players"]);
          Swal.fire('Nuevo jugador',`Jugador ${player.name} creado con éxito!`, 'success');
        },
        error: err => {
          this.errores = err.error.errors as string[];
          console.error('Codigo del error desde el backend: ' + err.status);
          console.error(err.error.errors);
        }
      }
    );
  }

  update() {
    this.playersService.update(this.player).subscribe({
        next: (player) => {
          this.router.navigate(["/players"]);
          Swal.fire('Jugador Actualizado', `Jugador ${player.name} actualizado por éxito`, 'success');
        },
        error: err => {
          this.errores = err.error.errors as string[];
          console.error('Codigo del error desde el backend: ' + err.status);
          console.error(err.error.errors);
        }
      }
    );
  }

  createlog() {
    console.log(this.player);
  }

}
