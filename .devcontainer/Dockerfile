FROM node:20

# Install basic development tools shared across projects.
RUN apt update && apt install -y less man-db sudo \
    && rm -rf /var/lib/apt/lists/*

# Ensure default `node` user has access to `sudo`.
ARG USERNAME=node
RUN echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Helps tools detect they are running inside a dev container.
ENV DEVCONTAINER=true
