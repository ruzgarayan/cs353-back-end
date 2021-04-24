package cs353.proje.usecases.loginregister.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
public class User {
    private int userId;
    private String email;
    private String username;
    private String password;
    private String telephone;
    private String image;
    private Date registrationDate;
    private String name;
    private String surname;
    private String userType;
}
