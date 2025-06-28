FROM ubuntu:24.04

SHELL ["/bin/bash", "-c"]
ENV DEBIAN_FRONTEND=noninteractive

# Install baseline packages
RUN apt-get update && \
    apt-get install --yes --no-install-recommends --no-install-suggests \
    bash \
    build-essential \
    curl \
    cmake \
    iputils-ping \
    gcc \
    gdb \
    git \
    g++ \
    libcurl4-gnutls-dev \
    libmpich-dev \
    libnuma-dev \
    locales \
    make \
    net-tools \
    netcat-openbsd \
    pkg-config \
    psmisc \
    python3 \
    python3-dbg \
    python3-pip \
    python3-setuptools \
    python3-wheel \
    python3-yaml \
    sudo \
    systemd \
    systemd-sysv \
    unzip \
    vim \
    wget \
    rsync \
    openssh-client \
    tmux && \
    rm -rf /var/lib/apt/lists/*

# Generate the desired locale (en_US.UTF-8)
RUN locale-gen en_US.UTF-8

# Make typing unicode characters in the terminal work.
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8

# Remove the `ubuntu` user and add a user `sss` so that you're not developing as
# the `root` user.
# NOTE: Replace `sss` with your own user name.
RUN userdel -r ubuntu && \
    useradd sss \
    --create-home \
    --shell=/bin/bash \
    --uid=1000 \
    --user-group && \
    echo "sss ALL=(ALL) NOPASSWD:ALL" >>/etc/sudoers.d/nopasswd

USER sss
