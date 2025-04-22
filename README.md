## FIAP Pós-Tech – Hackathon - Desafio
<h1 align="center"> Software Architecture | Grupo 34 – 8SOAT</h1>

### 📌 Introdução

Vocês foram contratados pela empresa FIAP X que precisa avançar no desenvolvimento de um projeto de processamento de imagens. Em uma rodada de investimentos, a empresa apresentou um projeto simples que processa um vídeo e retorna as imagens dele em um arquivo `.zip`.

Os investidores gostaram tanto do projeto, que querem investir em uma versão onde eles possam enviar um vídeo e fazer download deste `zip`.

Projeto utilizado na apresentação para os investidores:
[https://drive.google.com/file/d/1aYCnARmf1KMvRs_HUishp8LUYL_yPlMA/view?usp=sharing]

### 🚧 O Problema a Ser Resolvido

O projeto desenvolvido está sem nenhuma das boas práticas de arquitetura de software que nós aprendemos no curso.

### 🎯 Desafio

O seu desafio será desenvolver uma aplicação utilizando os conceitos apresentados no curso como: `desenho de arquitetura`, `desenvolvimento de microsservicos`, `qualidade de software`, `mensageria` e etc.

E para ajudar o seu grupo nesta etapa de levantamento de requisitos, segue alguns dos pré-requisitos esperados para este projeto:

* A nova versão do sistema deve processar mais de um vídeo ao mesmo tempo;
* Em caso de picos o sistema não deve perder uma requisição;
* O Sistema deve ser protegido por usuário e senha;
* O fluxo deve ter uma listagem de status dos vídeos de um usuário;
* Em caso de erro um usuário pode ser notificado (email ou um outro meio de comunicação).

### ⚙️ Requisitos técnicos

* O sistema deve persistir os dados;
* O sistema deve estar em uma arquitetura que o permita ser escalado;
* O projeto deve ser versionado no Github;
* O projeto deve ter testes que garantam a sua qualidade;
* CI/CD da aplicação.

### Entregáveis

* Documentação da arquitetura proposta para o projeto;
* Script de criação do banco de dados ou de outros recursos utilizados;
* Link do Github do(s) projeto(s);
* Vídeo de no máximo 10 minutos apresentando: Documentação, Arquitetura escolhida e o projeto funcionando.


### 💡 Hackathon - Solução

### Visão para a Área de Negócios

### Definição dos Requisitos

#### 🧩 Requisitos Funcionais Esperados

| Requisito | Solução |
| --- | --- |
| A nova versão do sistema deve processar mais de um vídeo ao mesmo tempo | A plataforma foi projetada para suportar o processamento simultâneo de múltiplos vídeos por meio da utilização de filas. Assim que um vídeo é enviado, ele é enfileirado para processamento assíncrono pela aplicação. Ao final do processamento, uma nova mensagem é registrada, informando a conclusão. Esse fluxo garante escalabilidade e evita gargalos, permitindo que diversos vídeos sejam processados em paralelo sem comprometer a performance do sistema. |
| A plataforma é projetada para não perder requisições mesmo durante picos de acesso | Utilizando o Kubernetes dentro do EKS (Elastic Kubernetes Service), configuramos recursos como Deployments, Services e o Horizontal Pod Autoscaler (HPA) para garantir o escalonamento automático da infraestrutura conforme a demanda. Quando há aumento no número de requisições, novos pods são provisionados automaticamente para manter a performance. Da mesma forma, em momentos de baixa demanda, os recursos são reduzidos, otimizando custos operacionais sem comprometer a disponibilidade. |
| O Sistema deve ser protegido por usuário e senha | A plataforma é acessível somente mediante cadastro prévio e aceitação dos termos de uso. Após a autenticação bem-sucedida, o Amazon Cognito emite tokens JWT (ID Token e Access Token), que são utilizados pelo API Gateway para autorizar o acesso aos recursos protegidos. Esses tokens funcionam como chaves de acesso, contendo informações codificadas e com tempo de expiração definido. Qualquer tentativa de acesso sem um token válido resulta em resposta automática de acesso negado por parte da plataforma. |
| O fluxo deve ter uma listagem de status dos vídeos de um usuário | A plataforma disponibiliza um endpoint autenticado que permite ao usuário visualizar a lista de vídeos associados à sua conta, juntamente com seus respectivos status. Para acessar esse recurso, é necessário que o usuário esteja autenticado previamente na plataforma por meio de login com usuário e senha. |
| Em caso de erro, um usuário pode ser notificado (email ou um outro meio de comunicação) | A plataforma conta com um sistema de notificação automatizado para falhas no processamento de vídeos. Quando ocorre um erro, a aplicação Java publica a mensagem em um tópico do Amazon SNS, que está configurado para enviar um e-mail ao usuário associado. Dessa forma, o usuário é informado sobre o erro e pode tomar as devidas ações, como tentar o envio novamente. |

### Para área de tecnologia

### Definição de requisitos técnicos

#### 🔐 Requisitos não funcionais

| Requisito | Solução |
| --- | --- |
| O sistema deve persistir os dados | A plataforma utiliza um banco de dados relacional MySQL para persistência das informações. São armazenados dados dos usuários cadastrados, informações sobre os vídeos, registros de acesso (logs), entre outros dados essenciais para o funcionamento e rastreabilidade do sistema.  |
| O sistema deve estar em uma arquitetura que permita escalabilidade | A plataforma foi desenvolvida com foco em escalabilidade e manutenibilidade, adotando os seguintes princípios: <ul><li>***Arquitetura Hexagonal:*** Estrutura que separa claramente a lógica de negócio dos elementos externos (como banco de dados, APIs e mensageria), facilitando testes, manutenção e evolução do sistema;</li><li>***Centralização da aplicação e infraestrutura:*** Embora a separação entre as camadas de aplicação e infraestrutura tenha sido considerada, optou-se por mantê-las centralizadas neste momento, visando maior simplicidade na gestão, implantação e monitoramento da aplicação — sem comprometer sua capacidade de escalar;</li><li>***Processamento assíncrono com filas:*** Utilização de filas e workers para executar tarefas intensivas de forma paralela, garantindo maior desempenho e evitando gargalos durante picos de uso;</li></ul> |
| O projeto deve ser versionado no Github | O código-fonte e os componentes do projeto estão versionados em um único repositório no GitHub. Você pode acessar o repositório principal do projeto através do seguinte link: [https://github.com/Lucas-lds/grupo34-gerador-thumbnail#](https://github.com/Lucas-lds/grupo34-gerador-thumbnail#)</li></ul> |
| O projeto deve ter testes que garantam sua qualidade | Foi configurado um GitHub Action para executar os testes automaticamente a cada push ou pull request, garantindo a validação contínua do código. Essa automação garante que o projeto mantenha sua qualidade e integridade ao longo do tempo. Os resultados dos testes podem ser acompanhados diretamente aqui: [url dos testes](url dos testes) |
| CI/CD da aplicação | O processo de CI/CD da aplicação pode ser acompanhado por meio do seguinte link [url do workflow](url do workflow) |

### 📃 Sobre o Projeto

A plataforma tem como objetivo gerenciar o envio e o processamento de vídeos, realizando a extração de quadros e gerando um arquivo compactado no formato .zip com as imagens resultantes. Além disso, oferece funcionalidades de autenticação e gerenciamento de usuários, garantindo segurança e controle de acesso aos recursos do sistema.


### 🧰 Tecnologias e Arquitetura

A stack foi escolhida pensando em escalabilidade, resiliência e manutenibilidade. A arquitetura segue um modelo **monolítico modular**, com base nos princípios de DDD e arquitetura hexagonal, o que facilita uma futura migração para microsserviços.

### Stack utilizada:

- **Domain Driven Design (DDD)**
- **Arquitetura Hexagonal**
- **Java**
- **MySQL**
- **Docker**
- **Kubernetes**
- **AWS (Amazon Web Services)**:
  - RDS
  - ECR
  - EKS
  - COGNITO
  - API-GATEWAY
  - LAMBDA
  - SQS
  - SNS
  - S3

### 📖 Documentação
 
A solução adota o Domain Driven Design (DDD) para entender e estruturar o domínio do negócio, focando na identificação e categorização dos subdomínios.

Como parte da documentação, foram desenvolvidos diversos artefatos para apoiar a equipe, incluindo:
- [🧠 Event Storming](https://miro.com/app/board/uXjVKxGC68M=/)  
- [📷 AWS](/documents/AWS/infra-aws-hackaton.jpg)
- [🎥 Vídeo](https://drive.google.com/file/d/19WEjt0IKTeJajN82UiRN8UGVSDA_Tzy2/view?usp=sharing)
- [📩 Collection](collection.json)
- [🧰 Script BD](docker/init.sql)
- [☁️ SonarQube]([docker/init.sql](https://sonarcloud.io/project/overview?id=Lucas-lds_grupo34-gerador-thumbnail))
 
### 💻 Instalação

Primeiro, clone o repositório para a sua máquina local:

```bash
git clone https://github.com/Lucas-lds/grupo34-gerador-thumbnail.git
```

**Premissas:**

 - Ter o Terraform instalado na máquina.
 - Possuir uma conta na AWS.

 ***⚙️ Passo a Passo (Setup AWS e Deploy)***

 1. Criar uma Conta na AWS:
  - Acesse: https://aws.amazon.com/
  - Clique em "Criar uma Conta da AWS".
  - Siga as instruções para completar o registro (será necessário um cartão de crédito — você pode usar o nível gratuito).

 2. Criar um Usuário IAM:
  - Acesse o Console AWS: AWS Management Console.
  - Navegue até IAM:
    - No console, procure por "IAM" (Identity and Access Management).
  - Criar Usuário:
    - Clique em "Users" e depois em "Add user".
  - Definir Permissões:
    - Escolha "Attach existing policies directly" e selecione políticas como AdministratorAccess (ou crie uma política personalizada conforme necessário).
  - Finalizar Criação:
    - Clique em "Create user" e anote a Access Key ID e Secret Access Key geradas. ⚠️ Importante: Guarde essas informações em local seguro.

 3. Instalar e Configurar o AWS CLI:
  - Instalação do AWS CLI: 
    - Siga as instruções de instalação para seu sistema operacional. Veja as https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
  - Configurar o AWS CLI:
    - Abra seu terminal ou prompt de comando.
    - Execute o comando:
    ```bash
    aws configure
    ```
    - Quando solicitado, insira:
    - **AWS Access Key ID:** (sua Access Key ID)
    - **AWS Secret Access Key:** (sua Secret Access Key)
    - **Default region name:** (exemplo: us-east-1)
    - **Default output format:** (opcional, como json)

  4. Testar a Configuração:
    - Para verificar se tudo está funcionando, execute:
    ```bash
    aws s3 ls
    ```      
    - Se estiver tudo configurado corretamente, você verá uma lista de seus buckets S3 (ou uma mensagem indicando que você não tem nenhum).

  ***🧪 Executando a Aplicação***  

  1. Criar o ECR (Elastic Container Registry)
  - Passo 1: Acesse a interface da AWS.
  - Passo 2: Navegue até o serviço ECR (Elastic Container Registry).
  - Passo 3: Crie um repositório com o nome repositorio.
    - **🔍 Observação: É necessário criar o repositório antes de fazer o push da imagem.**

  2. Preparar e Enviar a Imagem Docker
  - Passo 1: Acesse o diretório /docker onde se encontra o script para construção da imagem.
    ```bash
    cd /docker
    ``` 
  - Passo 2: Abra o script e preencha as seguintes variáveis com suas informações:
    - **AWS_REGION=** (ex: us-east-1) 
    - **ACCOUNT_ID=** (seu ID da conta AWS)  
    - **REPO_NAME=** (nome do repositório, neste caso repositorio)  
    - **IMAGE_TAG=** (tag desejada para a imagem, ex: latest)  
  - Passo 3: Execute o script para:
    - Construir a imagem
    - Taguear a imagem
    - Logar no ECR
    - Fazer o push da imagem para o ECR 

  3. Gerenciar Infraestrutura com Terraform
  - Passo 1: Navegue até o diretório /terraform utilizando o terminal:
    ```bash
    cd /terraform
    ``` 
  - Passo 2: Execute o comando para verificar a infraestrutura que será criada:
    ```bash
    terraform plan
    ```     
  - Passo 3: Para criar a infraestrutura, execute:
    ```bash
    terraform apply --auto-approve   
    ```   
  - Passo 4: Após a finalização do provisionamento de toda a infraestrutura na AWS, o terminal exibirá a URL da aplicação.
    - Para acessar a interface da aplicação, adicione o endpoint **/api/v1/swagger-ui/index.html** à URL e cole no navegador. 
    -**Exemplo:** a8ee83e4bc22a4019af48ebfa6656574-1293916010.us-east-1.elb.amazonaws.com/api/v1/swagger-ui/index.html   
  - Passo 5: Para excluir toda a infraestrutura na AWS, execute:
    ```bash
    terraform destroy --auto-approve
    ```

    ### ☁️ SonarQube
    ![image](https://github.com/user-attachments/assets/7850084c-ccbd-4030-9f59-18710fa19acf)

    - Cobertura de Testes com BDD:
    - ![image](https://github.com/user-attachments/assets/246bdb9f-ad9b-400a-9fc2-1e00c277bc13)

