import { Cell } from "./Cell";
import { AcGameObjects } from "./AcGameObject";


export class Snake extends AcGameObjects {
    constructor(info, gamemap){
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; // cells[0] is snake head
        this.next_cell = null;

        this.speed = 5;

        // 0, 1, 2, 3 represents up, right, down, left
        // -1 represents no input
        this.direction = -1; 
        
        // idle: no move, move: is moving, die: snake is dead
        this.status = "idle";

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        // num turn
        this.step = 0;

        this.eps = 1e-2;

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2;

        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ]

        this.eye_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ]
    }

    start() {
        
    }

    check_tail_increasing() {
        if (this.step <= 10){
            return true;
        }
        if (this.step % 3 === 1){
            return true;
        } else {
            return false;
        }
    }

    set_direction(d){
        this.direction = d;
    }

    next_step() {
        const d = this.direction;
        this.eye_direction = d;

        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        for (let i = k; i > 0; i --){
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
        
        // next movement is invalid
        // if (!this.gamemap.check_valid(this.next_cell)){
        //     this.status = "die";
        // }
    }

    update_move() {
        // distance between two frames
        const move_distance = this.speed * this.timedelta / 1000;
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps){
            // make the new cell to be the head
            this.status = "idle";
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            if (!this.check_tail_increasing()){
                this.cells.pop();
            }
        } else {
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
            if (!this.check_tail_increasing()){
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }


        

    }

    update() {
        if (this.status === "move"){
            // console.log("here");
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;

        if (this.status === "die"){
            ctx.fillStyle = "white";
        }
        for (const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, 0.8 * L / 2, 0, 2 * Math.PI);
            ctx.fill();
        }

        for (let i = 1; i < this.cells.length; i ++){
            const a = this.cells[i], b = this.cells[i - 1];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps){
                continue;
            }
            if (Math.abs(a.x - b.x) < this.eps){
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, 0.8 * L, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, 0.8 * L);
            }
        }

        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i ++){
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
        
    }
}