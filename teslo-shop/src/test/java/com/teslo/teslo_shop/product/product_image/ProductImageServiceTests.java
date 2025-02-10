package com.teslo.teslo_shop.product.product_image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.product.Product;
import com.teslo.teslo_shop.product.ProductRepository;
import com.teslo.teslo_shop.product.product_image.dto.ProductImageDto;

@ExtendWith(MockitoExtension.class)
public class ProductImageServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductImageService productImageService;

    @Test
    public void testFindAllByIds_ShouldReturnProductImages() {
        List<Long> ids = List.of(12345L, 12254L);
        Product product = new Product();
        List<ProductImage> productImages = List.of(new ProductImage("test1", product),
                new ProductImage("test2", product));

        when(productImageRepository.findAllById(ids)).thenReturn(Optional.of(productImages));

        List<ProductImageDto> result = productImageService.findAllByIds(ids);

        assertNotNull(result);
        assertEquals(productImages.size(), result.size());
        verify(productImageRepository, times(1)).findAllById(ids);
    }

    @Test
    public void testFindAllByIds_ShouldThrowNotFoundExceptionWhenProductImagesNotFound() {
        List<Long> ids = List.of(12345L, 12254L);
        String expectedMessage = "Not found all product images with these ids: " + ids;

        Exception exception = assertThrows(NotFoundException.class, () -> {
            productImageService.findAllByIds(ids);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindOne_ShouldReturnProductImage() {
        Long id = 12345L;
        Product product = new Product();
        ProductImage productImage = new ProductImage();
        productImage.setId(id);
        productImage.setUrl("test1");
        productImage.setProduct(product);

        when(productImageRepository.findById(id)).thenReturn(Optional.of(productImage));

        ProductImageDto result = productImageService.findOne(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(productImageRepository, times(1)).findById(id);
    }

    @Test
    public void testFindOne_ShouldThrowNotFoundExceptionWhenProductImageNotFound() {
        Long id = 12345L;
        String expectedMessage = "Not found product image with id: " + id;

        Exception exception = assertThrows(NotFoundException.class, () -> {
            productImageService.findOne(id);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindByProductId_ShouldReturnProductImages() {
        String productId = UUID.randomUUID().toString();
        Product product = new Product();
        product.setId(productId);
        List<ProductImage> productImages = List.of(new ProductImage("test1", product),
                new ProductImage("test2", product));

        when(productImageRepository.findByProductId(productId)).thenReturn(Optional.of(productImages));

        List<ProductImageDto> result = productImageService.findByProductId(productId);

        assertNotNull(result);
        assertEquals(productImages.size(), result.size());
        verify(productImageRepository, times(1)).findByProductId(productId);
    }

    @Test
    public void testFindByProductId_ShouldThrowNotFoundExceptionWhenProductNotFound() {
        String productId = UUID.randomUUID().toString();
        String expectedMessage = "Not found product images with product id: " + productId;

        Exception exception = assertThrows(NotFoundException.class, () -> {
            productImageService.findByProductId(productId);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindByProductIdAndUrlIn_ShouldReturnProductImages() {
        String productId = UUID.randomUUID().toString();
        List<String> urls = List.of("test1", "test2");
        Product product = new Product();
        product.setId(productId);
        List<ProductImage> productImages = urls.stream().map(url -> new ProductImage(url, product))
                .collect(Collectors.toList());

        when(productImageRepository.findByProductIdAndUrlIn(productId, urls)).thenReturn(Optional.of(productImages));

        List<ProductImageDto> result = productImageService.findByProductIdAndUrlIn(productId, urls);

        assertNotNull(result);
        assertEquals(productImages.size(), result.size());
        verify(productImageRepository, times(1)).findByProductIdAndUrlIn(productId, urls);
    }

    @Test
    public void testFindByProductIdAndUrlIn_ShouldThrowNotFoundExceptionWhenProductImagesNotFound() {
        String productId = UUID.randomUUID().toString();
        List<String> urls = List.of("test1", "test2");
        String expectedMessage = "Not found product images with urls: " + urls + " & product id: " + productId;

        Exception exception = assertThrows(NotFoundException.class, () -> {
            productImageService.findByProductIdAndUrlIn(productId, urls);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSave_ShouldCreateSampleProductImage() {
        Product product = new Product();
        ProductImage productImage = new ProductImage("test1", product);

        when(productImageRepository.save(productImage)).thenReturn(productImage);

        ProductImageDto result = productImageService.save(productImage);

        assertNotNull(result);
        verify(productImageRepository, times(1)).save(productImage);
    }

    @Test
    public void testSaveMultiple_ShouldCreateSeveralProducts() {
        Product product = new Product();
        List<ProductImage> productImages = List.of(new ProductImage("test1", product),
                new ProductImage("test2", product));

        when(productImageRepository.saveAll(anyList())).thenReturn(productImages);

        List<ProductImageDto> result = productImageService.saveMultiple(productImages);

        assertNotNull(result);
        assertEquals(productImages.size(), result.size());
        verify(productImageRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testDelete_ShouldReturnDeletedProductImage() throws JsonProcessingException {
        Long id = 12345L;
        String productId = UUID.randomUUID().toString();
        Product product = new Product();
        product.setId(productId);
        ProductImage productImage = new ProductImage("test1", product);
        productImage.setId(id);
        ProductImageDto productImageDto = new ProductImageDto(productImage);

        when(productImageRepository.findById(id)).thenReturn(Optional.of(productImage));
        when(objectMapper.convertValue(any(ProductImageDto.class), eq(ProductImage.class))).thenReturn(productImage);
        when(productRepository.findById(productImageDto.getProductId())).thenReturn(Optional.of(product));
        doNothing().when(productImageRepository).delete(productImage);

        ProductImageDto result = productImageService.delete(id);

        assertNotNull(result);
        assertEquals(productImageDto, result); // compare 2 ProductImageDto instances -> require @Override equals method
        assertEquals( // alternative to compare objects
                objectMapper.writeValueAsString(productImageDto),
                objectMapper.writeValueAsString(result));
        verify(productImageRepository, times(1)).findById(id);
        verify(productImageRepository, times(1)).delete(productImage);
    }

    @Test
    public void testDeleteMultiple_ShouldReturnDeletedProductImages() {
        List<Long> ids = List.of(12345L, 12254L);
        String productId = UUID.randomUUID().toString();
        List<String> urls = List.of("test1", "test2");
        Product product = new Product();
        product.setId(productId);
        List<ProductImage> productImages = urls.stream().map(url -> new ProductImage(url, product))
                .collect(Collectors.toList());
        for (int i = 0; i < productImages.size(); i++) {
            productImages.get(i).setId(ids.get(i));
        }
        List<ProductImageDto> productImageDtos = productImages.stream().map(ProductImageDto::new)
                .collect(Collectors.toList());

        when(productImageRepository.findAllById(ids)).thenReturn(Optional.of(productImages));
        when(objectMapper.convertValue(any(ProductImageDto.class), eq(ProductImage.class)))
                .thenAnswer(invocation -> {
                    ProductImageDto dto = invocation.getArgument(0);
                    /*
                     * ProductImage image = new ProductImage(dto.getUrl(), product);
                     * image.setId(dto.getId());
                     * return image;
                     */
                    return productImages.stream()
                            .filter(image -> image.getId().equals(dto.getId()))
                            .findFirst()
                            .orElse(null);
                });
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productImageRepository).deleteAll(productImages);

        List<ProductImageDto> result = productImageService.deleteMultiple(ids);

        assertNotNull(result);
        assertEquals(productImageDtos.size(), result.size());
        assertEquals(productImageDtos, result);
        verify(productImageRepository, times(1)).deleteAll(productImages);
    }

    @Test
    public void testDeleteByProductIdAndUrlIn__ShouldReturnDeletedProductImages() {
        List<Long> ids = List.of(12345L, 12254L);
        String productId = UUID.randomUUID().toString();
        List<String> urls = List.of("test1", "test2");
        Product product = new Product();
        product.setId(productId);
        List<ProductImage> productImages = urls.stream().map(url -> new ProductImage(url, product))
                .collect(Collectors.toList());
        for (int i = 0; i < productImages.size(); i++) {
            productImages.get(i).setId(ids.get(i));
        }
        List<ProductImageDto> productImageDtos = productImages.stream().map(ProductImageDto::new)
                .collect(Collectors.toList());

        when(productImageRepository.findByProductIdAndUrlIn(productId, urls)).thenReturn(Optional.of(productImages));
        when(objectMapper.convertValue(any(ProductImageDto.class), eq(ProductImage.class)))
                .thenAnswer(invocation -> {
                    ProductImageDto dto = invocation.getArgument(0);
                    return productImages.stream()
                            .filter(image -> image.getId().equals(dto.getId()))
                            .findFirst()
                            .orElse(null);
                });
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productImageRepository).deleteAll(productImages);

        List<ProductImageDto> result = productImageService.deleteByProductIdAndUrlIn(productId, urls);

        assertNotNull(result);
        assertEquals(productImageDtos.size(), result.size());
        assertEquals(productImageDtos, result);
        verify(productImageRepository, times(1)).deleteAll(productImages);
    }
}
