package dev.kuku.vekku.domains.auth.internal.repos.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "hashed_password", nullable = false)
  private String hashedPassword;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;
}
