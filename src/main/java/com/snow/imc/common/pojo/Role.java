package com.snow.imc.common.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Builder
@Entity
@Data
@Table(name = "s_role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(targetEntity = User.class,mappedBy = "roles")
    @JSONField(serialize = false)
    private List<User> users;
    @ManyToMany(cascade=CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns=@JoinColumn(name="authority_id"),
            joinColumns=@JoinColumn(name="role_id"))
    @JSONField(serialize = false)
    private List<Authority> authorities;





}
