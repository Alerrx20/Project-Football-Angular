import { Component } from '@angular/core';
import { TeamsService } from './teams.service';
import { ActivatedRoute } from '@angular/router';
import { Team } from './team';
import { tap } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.css']
})
export class TeamsComponent {

  teams: Team[] = [];
  paginator:any;

  constructor(private teamsService: TeamsService,
    private activatedRoute: ActivatedRoute) { }
  
  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        let page:number = Number(params.get('page'));
        if (!page) {
          page = 0;
        }
        this.teamsService.getTeams(page).pipe()
          .subscribe((response) => { 
            this.teams = response.content as Team[];
            this.paginator = response;
          })
      }
    );
  }
}
