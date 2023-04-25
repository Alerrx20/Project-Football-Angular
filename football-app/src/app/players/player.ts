import { Team } from "../teams/team";

export class Player {
    id!: number;
    name!: string;
    lastname!: string;
    email!: string;
    birthdate!: string;
    jerseyNumber!: number;
    photo!: string;
    team:Team = new Team();
}