package top.jyx365.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

import top.jyx365.authserver.domain.Group;

@Transactional
public interface GroupRepository extends JpaRepository<Group, String> {

}
