package top.jyx365.authserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@ToString(exclude="users")
@Table(name ="GROUPS")
@EqualsAndHashCode(of={"groupId"})
public class Group implements Serializable {

    private static final long serialVersionUID = -8366929034533774130L;

    public static final String GUEST_GROUP = "GUEST_GROUP";

    public static final String USER_GROUP = "USER_GROUP";

    public static final String ENTERPRISE_USER_GROUP = "ENTERPRISE_USER_GROUP";

    public static final String SYSTEM_OPERATOR_GROUP = "SYSTEM_OPERATOR_GROUP";

    public static final String ADMIN_GROUP = "ADMIN_GROUP";



    @Id
    @Column(length = 128)
    private String groupId;

    @Column(length = 256, nullable = false)
    private String groupName;

    @Column(length = 2000)
    private String groupDescription;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "group_authorities",
        joinColumns = {@JoinColumn(name = "group_id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name="group_users",
            joinColumns=@JoinColumn(name="GROUP_ID"),
            inverseJoinColumns=@JoinColumn(name="USER_ID"))
    private Set<User> users;

    public Group(String groupId) {
        this.groupId = groupId;
    }

    public Group() {
        authorities = new HashSet<>();
        users = new HashSet<>();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

}
