package com.teslo.teslo_shop.product;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.core.dto.PaginationDto;
import com.teslo.teslo_shop.core.error.exceptions.BadRequestException;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.core.helpers.CriteriaHelper;
import com.teslo.teslo_shop.core.utils.StringUtil;
import com.teslo.teslo_shop.product.dto.PlainProductDto;
import com.teslo.teslo_shop.product.dto.ProductDto;
import com.teslo.teslo_shop.product.enums.ProductModelEnum;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Transactional
@Service
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
    private final ProductRepository repository;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaHelper<Product> criteria;

    public ProductService(ProductRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * The {@code @PostConstruct} init method ensures that the {@code EntityManager}
     * has been injected before initialising {@code criteria}
     */
    @PostConstruct
    public void init() {
        this.criteria = new CriteriaHelper<Product>(entityManager.getCriteriaBuilder(), Product.class);
    }

    public List<PlainProductDto> findAll(PaginationDto paginationDto) {

        CriteriaQuery<Product> cq = this.getQuery();
        TypedQuery<Product> query = entityManager.createQuery(cq);

        query.setFirstResult(paginationDto.getOffset())
                .setMaxResults(paginationDto.getLimit());

        return query.getResultList().stream().map(product -> this.mapToDto(product))
                .collect(Collectors.toList());
    }

    public PlainProductDto findOne(String term) {

        LOGGER.log(Level.INFO, "findOne by term: \"" + term + "\"");

        CriteriaBuilder cb = criteria.cb();
        CriteriaQuery<Product> cq = this.getQuery();
        Root<Product> root = criteria.root();

        Optional<Product> optProduct = Optional.empty();

        if (StringUtil.isUUID(term))
            optProduct = this.repository.findById(term);
        else {
            cq.where(cb.or(
                    cb.equal(cb.upper(root.get(ProductModelEnum.TITLE.str())), term.toUpperCase()),
                    cb.equal(root.get(ProductModelEnum.SLUG.str()), term)));
            TypedQuery<Product> query = entityManager.createQuery(cq);
            optProduct = query.getResultList().stream().findFirst();
        }

        Product product = optProduct
                .orElseThrow(() -> new NotFoundException("Not found product with term(id, title or slug): " + term));
        return this.mapToDto(product);
    }

    public PlainProductDto save(Product product) {
        Product savedProduct = this.repository.save(product);
        return this.mapToDto(savedProduct);
    }

    public PlainProductDto save(PlainProductDto plainProductDto) {
        /**
         * @see
         *      this conversion works because uses ProductImageDto(String url)
         *      constructor
         */
        ProductDto productDto = objectMapper.convertValue(plainProductDto, ProductDto.class);
        Product product = objectMapper.convertValue(productDto, Product.class);
        Product savedProduct = this.repository.save(product);
        return this.mapToDto(savedProduct);
    }

    public List<PlainProductDto> saveMultiple(List<Product> products) {
        List<Product> savedProducts = this.repository.saveAll(products);
        return savedProducts.stream().map(product -> this.mapToDto(product))
                .collect(Collectors.toList());
    }

    public PlainProductDto update(String id, Product newProduct) throws BadRequestException {

        Product product = this.getByUuid(id);

        /**
         * @see
         * 
         *      Necessary that the class from which objects are instantiated has setters
         *      of the attributes to be updated.
         */
        BeanUtils.copyProperties(newProduct, product, "id");

        return this.save(product);
    }

    public PlainProductDto delete(String id) {

        Product product = this.getByUuid(id);
        this.repository.delete(product);
        return this.mapToDto(product);
    }

    public void deleteAll() {
        this.repository.deleteAllProducts();
    }

    private Product getByUuid(String id) {

        if (!StringUtil.isUUID(id))
            throw new BadRequestException("Invalid UUID");
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product with id: " + id));
    }

    private PlainProductDto mapToDto(Product product) {
        ProductDto productDto = objectMapper.convertValue(product, ProductDto.class);
        return new PlainProductDto(productDto);
    }

    private CriteriaQuery<Product> getQuery() {

        CriteriaQuery<Product> cq = this.criteria.cq();
        Root<Product> root = criteria.root();

        /*
         * Join<Product, ProductImage> productImageJoin =
         * root.join(ProductModelEnum.IMAGES.str(), JoinType.LEFT);
         */
        /*
         * Join<Product, User> userJoin = root.join(ProductModelEnum.USER.str(),
         * JoinType.LEFT);
         */

        /*
         * cq.multiselect(
         * root.get(ProductModelEnum.TITLE.str()),
         * root.get(ProductModelEnum.SLUG.str())
         * // productImageJoin.get(ProductImageModelEnum.URL.str()),
         * // userJoin.get(UserModelEnum.EMAIL.str())
         * );
         */
        cq.select(root.alias(ProductModelEnum.MAIN.str()));

        return cq;
    }
}
