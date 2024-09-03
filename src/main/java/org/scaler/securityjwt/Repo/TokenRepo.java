package org.scaler.securityjwt.Repo;


import org.scaler.securityjwt.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token,Long> {
    //Token save(Token t);
}
