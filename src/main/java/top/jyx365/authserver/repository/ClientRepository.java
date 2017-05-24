package top.jyx365.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

import top.jyx365.authserver.domain.Client;

@Transactional
public interface ClientRepository extends JpaRepository<Client, String> {

}
