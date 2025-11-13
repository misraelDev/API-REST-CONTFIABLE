package com.contfiable.security;

import com.contfiable.exception.BadRequestException;
import com.contfiable.model.User;
import com.contfiable.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtiene el usuario autenticado desde el SecurityContext
     * @return Usuario autenticado
     * @throws BadRequestException si no hay usuario autenticado
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No hay usuario autenticado");
        }

        Object principal = authentication.getPrincipal();
        String userId;
        
        if (principal instanceof UserDetails userDetails) {
            userId = userDetails.getUsername();
        } else if (principal instanceof String) {
            userId = (String) principal;
        } else {
            throw new BadRequestException("No se pudo obtener el ID del usuario autenticado");
        }

        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BadRequestException("Usuario autenticado no encontrado"));
    }

    /**
     * Obtiene el ID del usuario autenticado
     * @return ID del usuario autenticado
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}

