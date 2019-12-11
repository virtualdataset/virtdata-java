<template>
    <div class="namespaces">
        <pre v-text="$attrs"/>
    </div>
</template>

<script>
    export default {
        props: {
            namespaces: {}
        },
    };
</script>
