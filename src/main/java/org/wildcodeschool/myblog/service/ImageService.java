package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            return null;
        }
        return images.stream()
                .map(imageMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'image avec l'id " + id + " n'a pas été trouvé"));
        if (image == null) {
            return null;
        }
        return imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(@RequestBody Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage(@PathVariable Long id, @RequestBody Image imageDetails) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'image avec l'id " + id + " n'a pas été trouvé"));
        if (image == null) {
            return null;
        }
        image.setUrl(imageDetails.getUrl());
        Image updatedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'image avec l'id " + id + " n'a pas été trouvé"));
        if (image == null) {
            return false;
        }
        imageRepository.delete(image);
        return true;
    }
}
