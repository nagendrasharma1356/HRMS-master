package com.HolidayService.Repository;

import com.HolidayService.Entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday,Long>
{
   Holiday findByTitle(String title);
}
