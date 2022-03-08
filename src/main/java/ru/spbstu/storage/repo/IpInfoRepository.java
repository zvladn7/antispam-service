package ru.spbstu.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.spbstu.storage.dto.IpInfo;

@Component
public interface IpInfoRepository extends JpaRepository<IpInfo, Long> {
}
