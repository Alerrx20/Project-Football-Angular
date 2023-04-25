import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PlayersComponent } from './players/players.component';
import { PlayerFormComponent } from './players/player-form/player-form.component';
import { LoginComponent } from './login/login.component';
import { TeamsComponent } from './teams/teams.component';
import { FormTeamComponent } from './teams/form-team/form-team.component';
import { AuthGuard } from './helpers/auth.guard';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: '', redirectTo: 'players', pathMatch: 'full'},
  {path: 'players', component: PlayersComponent, canActivate: [AuthGuard]},
  {path: 'players/page/:page', component: PlayersComponent, canActivate: [AuthGuard]},
  {path: 'players/form', component: PlayerFormComponent, canActivate: [AuthGuard]},
  {path: 'players/form/:id', component: PlayerFormComponent, canActivate: [AuthGuard]},
  {path: 'teams', component: TeamsComponent, canActivate: [AuthGuard]},
  {path: 'teams/page/:page', component: TeamsComponent, canActivate: [AuthGuard]},
  {path: 'teams/form', component: FormTeamComponent, canActivate: [AuthGuard]},
  {path: 'teams/form/:id', component: FormTeamComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
