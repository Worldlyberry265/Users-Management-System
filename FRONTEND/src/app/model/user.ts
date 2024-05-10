
export class User {

    constructor(private id: number,
                private username: string,
                private password: string,
                private email: string,
                private address: string,
                private roles: string) {

    }
    getId() : number {
        return this.id;
    }
    getUsername(): string {
        return this.username;
    }
    getEmail() {
        return this.email;
    }
    getPassword() {
        return this.password;
    }
    getAddress() {
        return this.address;
    }
    getRoles() {
        return this.roles;
    }
}

