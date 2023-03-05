# MovieList-backend
-----
## Spring boot info
*Writing of spring boot application*
1. Spring intializr , creation of spring boot add dependencies + other informations for the application
2. Pom.xml, like package.json check dependencies and application information are correct
3. Writing of application, concepts: dto -> dao -> service -> controller
- dto : data transfer object, 
    1. dto, have Document leads to collection
    2. Data annotation bundles getter and setter
    3. All args Constructor generates a constructor with one parameters for every fields
    4. no args Constructor generates a constructor with no parameters for a class
```
dto, content types, eg: Review.java (dto)
@Document(collection = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    private ObjectId id;
    private String body;

    public Review(String body) {
        this.body = body;
    }
}
```
- dao: data access object,
    1. connection to data base to access object
    2. Here we use MongoRepository for the connection and the connection for the database happens in application.properties and secret info present in .env (not present in git because of security, ignored by .gitignore)
```
Repository interface, eg: ReviewRepository.java (dao)
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
}
```
- service
    1. Annotated with service
    2. Autowired to dao
    3. using object of dao class to perform the action of the db (query like insert, where and push)
```
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody,String imdbId){
        Review review = new Review(reviewBody);
        reviewRepository.insert(review);

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();
        return review;
    }
}
```
- controller
    1. Annotated with RestController
    2. RequestMapping for the api query path
    3. Get or post through mapping
    4. Always with ResponseEntity and its type
    5. Return of the responseEntity along with type and HTTPStatus
```
@RestController
@RequestMapping("/api/v1/movies")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload){
        return new ResponseEntity<Review>(reviewService.createReview(payload.get("reviewBody"), payload.get("imdbId")), HttpStatus.OK);
    }
}
```
-------
* Above Information is for the spring boot
* Do see the Reactjs contivity of this application