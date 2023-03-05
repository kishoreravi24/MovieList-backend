package dev.kishore.movies.dao;

import dev.kishore.movies.dto.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {

}
