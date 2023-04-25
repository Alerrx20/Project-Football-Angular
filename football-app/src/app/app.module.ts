import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PlayersComponent } from './players/players.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { DetailComponent } from './players/detail/detail.component';
import { PlayersService } from './players/players.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { LOCALE_ID, NgModule } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeES from '@angular/common/locales/es';
import { PaginatorComponent } from './paginator/paginator.component';
import { PlayerFormComponent } from './players/player-form/player-form.component';
import { LoginComponent } from './login/login.component';
import { TeamsComponent } from './teams/teams.component';
import { PaginatorTeamComponent } from './teams/paginator-team/paginator-team.component';
import { FormTeamComponent } from './teams/form-team/form-team.component';
import { AuthInterceptor } from './helpers/auth.interceptor';

registerLocaleData(localeES, 'es');

@NgModule({
  declarations: [
    AppComponent,
    PlayersComponent,
    HeaderComponent,
    FooterComponent,
    DetailComponent,
    PaginatorComponent,
    PlayerFormComponent,
    LoginComponent,
    TeamsComponent,
    PaginatorTeamComponent,
    FormTeamComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDatepickerModule, 
    MatNativeDateModule,
    MatInputModule,
    MatFormFieldModule
  ],
  providers: [
    PlayersService, 
    {provide: LOCALE_ID, useValue: 'es' },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
