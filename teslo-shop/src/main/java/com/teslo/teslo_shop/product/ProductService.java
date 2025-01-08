package com.teslo.teslo_shop.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.core.utils.StringUtil;
import com.teslo.teslo_shop.product.dto.ProductDto;
import com.teslo.teslo_shop.product.enums.ProductModelEnum;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository repository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;

    }

    public List<ProductDto> findAll() {
        List<Product> products = this.repository.findAll();
        return products.stream().map(product -> this.mapToDto(product))
                .collect(Collectors.toList());
    }

    public ProductDto findOne(String term) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = this.getQuery();
        Root<Product> producRoot = cq.from(Product.class);

        Product product = null;

        if (StringUtil.isUUID(term))
            product = this.repository.findById(term).orElse(null);
        else {

            cq.where(cb.or(
                    cb.equal(cb.upper(producRoot.get(ProductModelEnum.TITLE.str())), term.toUpperCase()),
                    cb.equal(producRoot.get(ProductModelEnum.SLUG.str()), term)));
            TypedQuery<Product> query = entityManager.createQuery(cq);
            product = query.getResultList().stream().findFirst().orElse(null);
        }

        if (product == null)
            throw new NotFoundException("Not found product with term(id, title or slug): " + term);

        return this.mapToDto(product);
    }

    public ProductDto create(Product product) {
        this.repository.save(product);
        return this.mapToDto(product);
    }

    private ProductDto mapToDto(Product product) {
        return objectMapper.convertValue(product, ProductDto.class);
    }

    private CriteriaQuery<Product> getQuery() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> producRoot = cq.from(Product.class);

        /*
         * Join<Product, ProductImage> productImageJoin =
         * producRoot.join(ProductModelEnum.IMAGES.str(), JoinType.LEFT);
         */
        /*
         * Join<Product, User> userJoin = producRoot.join(ProductModelEnum.USER.str(),
         * JoinType.LEFT);
         */

        /*
         * cq.multiselect(
         * producRoot.get(ProductModelEnum.TITLE.str()),
         * producRoot.get(ProductModelEnum.SLUG.str())
         * // productImageJoin.get(ProductImageModelEnum.URL.str()),
         * // userJoin.get(UserModelEnum.EMAIL.str())
         * );
         */
        cq.select(producRoot.alias(ProductModelEnum.MAIN.str()));

        return cq;
    }
}
