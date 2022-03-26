package ru.spbstu.storage.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.spbstu.storage.postgres.dto.UserInfo;

@Component
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
