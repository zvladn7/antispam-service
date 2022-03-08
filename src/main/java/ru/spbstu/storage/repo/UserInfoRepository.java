package ru.spbstu.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.spbstu.storage.dto.UserInfo;

@Component
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
