package com.fiap.geradorThumbnail.application.port.out;

import com.fiap.geradorThumbnail.core.dto.UsuarioCognitoRequest;
import com.fiap.geradorThumbnail.infrastructure.exception.CadastroCognitoException;
import com.fiap.geradorThumbnail.infrastructure.exception.UsuarioPossuiCadastroCognito;

public interface CognitoAdapterPortOut {
    String cadastrarUsuarioCognito(UsuarioCognitoRequest usuario) throws UsuarioPossuiCadastroCognito, CadastroCognitoException;
}
