package live.jrmd.sidecar;

import live.jrmd.sidecar.services.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usersLoader) // How to find users by their username
                .passwordEncoder(passwordEncoder()) // How to encode and verify passwords
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()

                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")

                .and()
                .authorizeRequests()
                .antMatchers("/",
                                        "/routes",
//                                        "/routes/create",
                                        "/points",
//                                        "/points/add",
                                        "/events",
//                                        "/events/create",
                                        "/js/**",
                                        "/css/**"
                )
                .permitAll()

                .and()
                .authorizeRequests()
                .antMatchers(

                        "/route/{id}/edit",
                        "/routes/create",
                        "/events/create",
                        "/event/{id}/edit",
                        "/points/add",
                        "/point/{id}/edit",
                        "/profile",
                        "/user/{id}/edit"
                )
                .authenticated()
        ;
    }
}
