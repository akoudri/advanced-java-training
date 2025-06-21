#!/bin/bash

comm -23 \
  <(git ls-tree -r --name-only main | sort) \
  <(git ls-tree -r --name-only solutions | sort)

