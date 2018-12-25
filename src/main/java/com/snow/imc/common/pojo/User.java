package com.snow.imc.common.pojo;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Builder
@Table(name = "s_user",uniqueConstraints = {@UniqueConstraint(columnNames = "username")},indexes = {@Index(columnList = "username")})
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String img;
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ManyToMany(targetEntity = Role.class,fetch = FetchType.EAGER)
    @JoinTable(joinColumns={@JoinColumn(name="user_id")},
            inverseJoinColumns={@JoinColumn(name="role_id")})
    @JSONField(serialize = false)
    private List<Role> roles=new ArrayList<>();

    @Transient
    private String token;

}