package de.adesso.kicker.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, String> {
    int countAllByRatingAfter(int rating);
}
