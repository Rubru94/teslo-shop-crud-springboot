package com.teslo.teslo_shop.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.core.dto.PaginationDto;
import com.teslo.teslo_shop.core.error.exceptions.BadRequestException;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.product.dto.PlainProductDto;
import com.teslo.teslo_shop.product.product_image.ProductImage;
import com.teslo.teslo_shop.product.product_image.ProductImageService;
import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageService productImageService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> query;

    @InjectMocks
    private ProductService productService;

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Product> criteriaQuery;
    private Root<Product> root;

    @BeforeEach
    public void setUp() {
        criteriaBuilder = mock(CriteriaBuilder.class);
        criteriaQuery = mock(CriteriaQuery.class);
        root = mock(Root.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Product.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Product.class)).thenReturn(root);

        productService = new ProductService(productRepository, productImageService);
        productService.setEntityManager(entityManager);
        productService.init();
    }

    @Test
    public void testFindAll_ShouldReturnNoProducts() {
        PaginationDto paginationDto = new PaginationDto(10, 5);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.setFirstResult(paginationDto.getOffset())).thenReturn(query);
        when(query.setMaxResults(paginationDto.getLimit())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<PlainProductDto> result = productService.findAll(paginationDto);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(query, times(1)).setFirstResult(paginationDto.getOffset());
        verify(query, times(1)).setMaxResults(paginationDto.getLimit());
        verify(query, times(1)).getResultList();
    }

    @Test
    public void testFindAll_ShouldReturnProducts() {
        PaginationDto paginationDto = new PaginationDto();
        List<Product> products = List.of(new Product(), new Product());

        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.setFirstResult(paginationDto.getOffset())).thenReturn(query);
        when(query.setMaxResults(paginationDto.getLimit())).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<PlainProductDto> result = productService.findAll(paginationDto);

        assertNotNull(result);
        assertEquals(products.size(), result.size());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(query, times(1)).setFirstResult(paginationDto.getOffset());
        verify(query, times(1)).setMaxResults(paginationDto.getLimit());
        verify(query, times(1)).getResultList();
    }

    @Test
    public void testFindOne_ShouldReturnProductWhenTermIsUUID() {
        // arrange
        String id = UUID.randomUUID().toString();
        Product product = new Product();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // act
        PlainProductDto result = productService.findOne(id);

        // assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    public void testFindOne_ShouldReturnProductWhenTermIsTitle() {
        String title = "Test Title";
        Product product = new Product();
        product.setTitle(title);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(product)); // same as Arrays.asList(product)

        PlainProductDto result = productService.findOne(title);

        assertNotNull(result);
        assertEquals(title, result.getTitle());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
    }

    @Test
    public void testFindOne_ShouldReturnProductWhenTermIsSlug() {
        String slug = "test-slug";
        Product product = new Product();
        product.setSlug(slug);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(product));

        PlainProductDto result = productService.findOne(slug);

        assertNotNull(result);
        assertEquals(slug, result.getSlug());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
    }

    @Test
    public void testFindOne_ShouldThrowNotFoundExceptionWhenProductNotFound() {
        String term = "non-existing";
        String expectedMessage = "Not found product with term(id, title or slug): " + term;

        when(entityManager.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            productService.findOne(term);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSave_ShouldCreateSampleProduct() {
        Product product = new Product();

        when(productRepository.save(product)).thenReturn(product);

        PlainProductDto result = productService.save(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testSave_ShouldCreateProductWithRelatedUser() {
        PlainProductDto plainProductDto = new PlainProductDto();
        User user = new User();
        Product product = new Product(plainProductDto, user);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        PlainProductDto result = productService.save(plainProductDto, user);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testSaveMultiple_ShouldCreateSeveralProducts() {
        List<Product> products = List.of(new Product(), new Product());
        List<User> users = List.of(new User(), new User());

        when(productRepository.saveAll(anyList())).thenReturn(products);

        List<PlainProductDto> result = productService.saveMultiple(products, users);

        assertNotNull(result);
        assertEquals(products.size(), result.size());
        verify(productRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testUpdate_ShouldThrowBadRequestExceptionWithInvalidUUID() {
        String invalidProductId = "invalid-uuid";
        String expectedMessage = "Invalid UUID";
        PlainProductDto newProduct = new PlainProductDto();

        Exception exception = assertThrows(BadRequestException.class, () -> {
            productService.update(invalidProductId, newProduct);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(productRepository, never()).findById(anyString());
        verify(productImageService, never()).findByProductId(anyString());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdate_ShouldThrowNotFoundExceptionWhenProductNotFound() {
        String productId = UUID.randomUUID().toString();
        String expectedMessage = "Not found product with id: " + productId;
        PlainProductDto newProduct = new PlainProductDto();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            productService.update(productId, newProduct);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(productRepository, times(1)).findById(productId);
        verify(productImageService, never()).findByProductId(anyString());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdate_ShouldReturnUpdateProductWithNewProperties() {
        String productId = UUID.randomUUID().toString();
        String newTitle = "New Title";
        String newDescription = "New Description";
        PlainProductDto newProduct = new PlainProductDto();
        newProduct.setTitle(newTitle);
        newProduct.setDescription(newDescription);
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageService.findByProductId(productId)).thenReturn(new ArrayList<>());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        PlainProductDto result = productService.update(productId, newProduct);

        assertNotNull(result);
        assertEquals(newTitle, result.getTitle());
        assertEquals(newDescription, result.getDescription());
        verify(productRepository, times(1)).findById(productId);
        verify(productImageService, times(1)).findByProductId(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdate_ShouldReturnUpdateProductWithNewImages() {
        String productId = UUID.randomUUID().toString();
        PlainProductDto newProduct = new PlainProductDto();
        newProduct.setImages(List.of("newImage1.jpg", "newImage2.jpg"));
        Product product = new Product();
        product.setId(productId);
        List<ProductImageDto> existingImages = List.of(new ProductImage("oldImage1.jpg", product),
                new ProductImage("oldImage2.jpg", product)).stream().map(ProductImageDto::new)
                .collect(Collectors.toList());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageService.findByProductId(productId)).thenReturn(existingImages);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        PlainProductDto result = productService.update(productId, newProduct);

        assertNotNull(result);
        assertEquals(newProduct.getImages().size(), result.getImages().size());
        verify(productRepository, times(1)).findById(productId);
        verify(productImageService, times(1)).findByProductId(productId);
        verify(productImageService, times(1)).deleteByProductIdAndUrlIn(eq(productId), anyList());
        verify(productImageService, times(1)).saveMultiple(anyList());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdate_ShouldReturnUpdateProductWithDeletedImages() throws BadRequestException {
        String productId = UUID.randomUUID().toString();
        PlainProductDto newProduct = new PlainProductDto();
        newProduct.setImages(List.of("newImage1.jpg"));
        Product product = new Product();
        product.setId(productId);

        List<ProductImageDto> existingImages = List.of(new ProductImage("oldImage1.jpg", product),
                new ProductImage("oldImage2.jpg", product)).stream().map(ProductImageDto::new)
                .collect(Collectors.toList());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productImageService.findByProductId(productId)).thenReturn(existingImages);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        PlainProductDto result = productService.update(productId, newProduct);

        assertNotNull(result);
        assertEquals(newProduct.getImages().size(), result.getImages().size());
        verify(productRepository, times(1)).findById(productId);
        verify(productImageService, times(1)).findByProductId(productId);
        verify(productImageService, times(1)).deleteByProductIdAndUrlIn(eq(productId), anyList());
        verify(productImageService, times(1)).saveMultiple(anyList());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDelete_ShouldReturnDeletedProduct() {
        String productId = UUID.randomUUID().toString();
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        PlainProductDto result = productService.delete(productId);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteAll_ShouldDeleteAllProducts() {
        doNothing().when(productRepository).deleteAllProducts();

        productService.deleteAll();

        verify(productRepository, times(1)).deleteAllProducts();
    }
}
