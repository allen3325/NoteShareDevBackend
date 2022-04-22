package ntou.notesharedevbackend.verificationModule.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 這個標記已經含有 @Configuration標記了
// 注入(@Autowired) UserDetailsService, Spring 會自動找到有實作這個介面的類別，也就是 SpringUserService
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override // setting the rules of API's authentication
    protected void configure(HttpSecurity http) throws Exception {
        //「/users」這個 API 及其底下的所有 GET 請求，需通過身份驗證才可存取。
        // 其餘 API 的所有GET請求，允許所有呼叫方存取。
        //「/users」這個 API 的 POST 請求，允許所有呼叫方存取。
        // 其餘的所有 API，需通過身份驗證才可存取。
        // authorizeRequests 方法開始自訂授權規則。使用 antMatchers 方法，傳入 HTTP 請求方法與 API 路徑，後面接著授權方式，
        http
                .authorizeHttpRequests()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/user").permitAll()
//                .anyRequest().authenticated() // anyRequest(): 對剩下的 API 定義規則
                .antMatchers(HttpMethod.POST, "/verification").permitAll()
                .antMatchers(HttpMethod.POST, "/verification/parse").permitAll()
                .anyRequest().permitAll() //TODO tmp for testing
                .and()
                .csrf().disable();// 關閉對 CSRF（跨站請求偽造）攻擊的防護 -> postman
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
