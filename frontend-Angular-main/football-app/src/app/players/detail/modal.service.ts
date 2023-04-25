import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  modal: Boolean = false;
  notifyUpload = new EventEmitter<any>();

  constructor() { }

  openModal() {
    this.modal = true;
  }

  closeModal() {
    this.modal = false;
  }

}
