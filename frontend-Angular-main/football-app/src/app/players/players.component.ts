import { Component } from '@angular/core';
import { Player } from './player';
import { PlayersService } from './players.service';
import { ActivatedRoute } from '@angular/router';
import { tap } from 'rxjs';
import Swal from 'sweetalert2';
import { ModalService } from './detail/modal.service';

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent {

  players: Player[] = [];
  paginator:any;
  selectedPlayer!: Player;

  constructor(private playersService: PlayersService,
    private activatedRoute: ActivatedRoute,
    private modalService: ModalService) {}
  
  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(
      params => {
        let page:number = Number(params.get('page'));
        if (!page) {
          page = 0;
        }
        this.playersService.getplayers(page).pipe(
          tap(response => {
            console.log("Jugador Component: tap 3");
            (response.content as Player[]).forEach(player => {
              console.log(player.name);
            });
          }))
          .subscribe((response) => { 
            this.players = response.content as Player[];
            this.paginator = response;
          })

          this.modalService.notifyUpload.subscribe(player => {
            console.log("modal----" + player);
            this.players = this.players.map(originalPlayer => {
              if (player.id == originalPlayer.id) {
                originalPlayer.photo = player.photo;
              }
              return originalPlayer;
            });
          })
      }
    );
  }

  delete(player: Player) {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    })
    
    swalWithBootstrapButtons.fire({
      title: 'Estás seguro?',
      text: `Seguro que deseas eliminar al jugador ${player.name} ${player.lastname}!`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar!',
      cancelButtonText: 'No, cancelar!',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.playersService.delete(player.id).subscribe(response => {
            this.players = this.players.filter( p => p !== player);
            swalWithBootstrapButtons.fire(
              'Jugador Eliminado!',
              `Jugador ${player.name} eliminado con éxito.`,
              'success'
            )
          }
        );
      }
    })
  }

  openModal(player: Player) {
    this.selectedPlayer = player;
    this.modalService.openModal();
  }

}
