import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { TeamsService } from '../teams.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Team } from '../team';

@Component({
  selector: 'app-form-team',
  templateUrl: './form-team.component.html',
  styleUrls: ['./form-team.component.css']
})
export class FormTeamComponent {

  title = "Crear Jugador";
  team = new Team();
  errores: string [] = [];

  constructor(private teamsService: TeamsService, 
    private router: Router,
     private activatedRoute: ActivatedRoute) {}
    
     ngOnInit() {
      this.loadTeam();
    }
  
    loadTeam() {
      this.activatedRoute.params.subscribe(params  => {
        let id = params['id'];
        if (id) {
          this.teamsService.getTeam(id).subscribe((team) => this.team = team);
        }
      });
    }

    create() {
      this.teamsService.create(this.team).subscribe({
          next: (team) => {
            this.router.navigate(["/teams"]);
            Swal.fire('Nuevo equipo',`Equipo ${team.name} creado con éxito!`, 'success');
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
      this.teamsService.update(this.team).subscribe({
          next: (team) => {
            this.router.navigate(["/teams"]);
            Swal.fire('Equipo Actualizado', `Equipo ${team.name} actualizado por éxito`, 'success');
          },
          error: err => {
            this.errores = err.error.errors as string[];
            console.error('Codigo del error desde el backend: ' + err.status);
            console.error(err.error.errors);
          }
        }
      );
    }

}
