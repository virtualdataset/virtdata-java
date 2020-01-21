#!/bin/bash
rm -rf dist .nuxt
npm run generate
rm -rf ../../resources/dsbench-guidebook
mv dist ../../resources/dsbench-guidebook
