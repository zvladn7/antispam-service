package ru.spbstu.storage.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spbstu.storage.postgres.dto.IpEntryDTO;

public interface IpEntryRepository extends JpaRepository<IpEntryDTO, Long> {
}
