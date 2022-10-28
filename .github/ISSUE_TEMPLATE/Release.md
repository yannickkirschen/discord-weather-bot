---
name: Release
about: Use this template for creating a new release.
title: "Release: x.y.z"
labels: release
assignees: yannickkirschen
---

# Prerequisites

- [ ] All changes for the release have been merged (rebased) into `main`.
- [ ] All workflows succeeded.

# Steps


- [ ] Checkout branch `main` and pull latest changes:
    - `git checkout main`
    - `git pull origin main`
- [ ] Set version in `pom.xml` to `x.y.z`.
- [ ] Rename "Next" section in `CHANGELOG.md` to `x.y.z (Month Day, Year)`.
- [ ] Commit and push your changes:
    - `git add .`
    - `git commit -s -m "build(release): x.y.z"`
    - `git push origin main`
- [ ] Check if all workflows succeeed.
- [ ] Publish release `x.y.z`.


- [ ] Checkout branch `main` and pull latest changes:
    - `git checkout main`
    - `git pull origin main`
- [ ] Set version in `pom.xml` to `x.y+1.z-SNAPSHOT`.
- [ ] Add "Next" section in `CHANGELOG.md`.
- [ ] Commit and push your changes:
    - `git add .`
    - `git commit -s -m "build(release): next development version x.y+1.z"`
    - `git push origin main`
- [ ] Check if all workflows succeed.

