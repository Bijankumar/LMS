package com.te.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.lms.entity.MockRating;
@Repository
public interface LmsMockRatingRepository extends JpaRepository<MockRating, Integer> {

}
