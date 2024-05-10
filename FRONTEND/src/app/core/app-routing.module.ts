import { NgModule } from "@angular/core";
import { Routes , RouterModule} from "@angular/router";
import { LoginComponent } from "../logging/login.component";
import { HomePageComponent } from "../home-page/home=page.component";
import { SignupComponent } from "../signup/signup.component";
import { AuthGuard } from "./auth/guards/guards.service";
import { EditUserComponent } from "../home-page/users-list/edit-user/edit-user.component";
import { UsersListComponent } from "../home-page/users-list/users-list.component";
import { UserResolverService } from "./user-resolver.service";


const appRoutes: Routes = [
    { path: 'login' , component: LoginComponent},
    { path: 'dashboard' , canActivate: [AuthGuard], component: HomePageComponent
     , children: [
        { path: 'usersList' , component: UsersListComponent, children: [
            { path: 'editUser/:id' , component: EditUserComponent , resolve: {user: UserResolverService}}
        ]}      
        ]
    },
    
    { path: 'signup' , component: SignupComponent},
    { path: '' , redirectTo: 'login', pathMatch: 'full'},
]

@NgModule({
imports: [RouterModule.forRoot(appRoutes)],
exports: [RouterModule],
})
export class AppRoutingModule {

}