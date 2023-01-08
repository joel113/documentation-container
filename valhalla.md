# Valhalla Docker Documentation

`nerdctl login ghcr.io`

`nerdctl pull ghcr.io/gis-ops/docker-valhalla/valhalla:latest`

`nerdctl run -it --rm valhalla`

`nerdctl run -it --rm valhalla:latest valhalla`

`nerdctl run -it --name valhalla -p 8002:8002 -v $PWD/valhalla:/custom_files/ -e serve_tiles=True -e build_admins=True ghcr.io/gis-ops/docker-valhalla/valhalla:latest`

`nerdctl exec -it --name valhalla /bin/sh`

## References

https://ikespand.github.io/posts/meili/

https://github.com/gis-ops/docker-valhalla

https://github.com/gis-ops/docker-valhalla#container-recipes

https://stackoverflow.com/questions/66159042/using-docker-image-from-github-registry-is-unauthorized

https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry

https://docs.github.com/en/packages/learn-github-packages/introduction-to-github-packages#authenticating-to-github-packages

https://docs.github.com/de/packages/working-with-a-github-packages-registry/working-with-the-container-registry
