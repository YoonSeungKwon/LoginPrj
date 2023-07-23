package yoon.test.loginPage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import yoon.test.loginPage.enums.Provider;
import yoon.test.loginPage.enums.Role;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 250)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @CreationTimestamp
    private LocalDateTime regdate;

    private String refreshToken;

    @Builder
    public Members(String email, String password, String name, Role role, Provider provider, String refreshToken){
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
