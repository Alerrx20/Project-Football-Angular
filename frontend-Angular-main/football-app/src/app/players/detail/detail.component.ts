import { Component, Input } from '@angular/core';
import Swal from 'sweetalert2';
import { HttpEventType } from '@angular/common/http';
import { ModalService } from './modal.service';
import { Player } from '../player';
import { PlayersService } from '../players.service';

@Component({
  selector: 'player-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent {

  title = "Detalle del Jugador";
  @Input() player!: Player;
  selectedPhoto: File | null = null;
  progress: number = 0;

  constructor(private playersService: PlayersService,
    public modalService: ModalService) {}

  selectPhoto(event: any) {
    this.selectedPhoto = event.target.files[0];
    this.progress = 0;
    console.log(this.selectedPhoto);
    if (this.selectedPhoto && this.selectedPhoto.type.indexOf('image') < 0) {
      Swal.fire('Error al seleccionar imagen: ', 'El archivo debe ser del tipo imagen', 'error');
      this.selectedPhoto = null;
    }
  }

  uploadPhoto() {
    if (!this.selectedPhoto) {
      Swal.fire('Error Upload: ', 'Debes seleccionar una foto', 'error');
    } else {
      this.playersService.uploadPhoto(this.selectedPhoto, this.player.id)
      .subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = event.total ? Math.round(100 * event.loaded / event.total) : 0;
        } else if(event.type === HttpEventType.Response) {
          let response: any = event.body;
          this.player = response.player as Player;
          console.log(this.player);
          this.modalService.notifyUpload.emit(this.player);
          Swal.fire('La foto se ha subido completamente!', response.message , 'success');
        }
      });
    }
  }

  closeModal() {
    this.modalService.closeModal();
    this.selectedPhoto = null;
    this.progress = 0;
  }

}
