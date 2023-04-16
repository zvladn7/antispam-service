// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.storage.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spbstu.storage.postgres.dto.IpEntryDTO;

public interface IpEntryRepository extends JpaRepository<IpEntryDTO, Long> {
}
