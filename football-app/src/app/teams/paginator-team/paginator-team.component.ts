import { Component, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-paginator-team',
  templateUrl: './paginator-team.component.html'
})
export class PaginatorTeamComponent {

  @Input() paginator: any;
  pages: number[] = [];
  from!: number;
  to!: number;

  ngOnInit() {
    this.initPaginator();
  }

  ngOnChanges(changes: SimpleChanges) {
    let updatedPaginator = changes['paginator'];
    if (updatedPaginator.previousValue) {
      this.initPaginator();
    }
  }

  private initPaginator() {
    this.from = Math.min(Math.max(1,this.paginator.number-4), this.paginator.totalPages-5);
    this.to = Math.max(Math.min(this.paginator.totalPages,this.paginator.number+4), 6);
    if (this.paginator.totalPages > 5) {
      this.pages = new Array(this.to - this.from + 1).fill(0).map((valor, indice) => indice + this.from);
    } else {
      this.pages = new Array(this.paginator.totalPages).fill(0).map((valor, indice) => indice + 1);
    }
  }
}
