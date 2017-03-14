export class CurrentUser {
    constructor( public username: string,
        public password: string,
        public jwt: string,
        public jwtExpires: number ) { }
}
