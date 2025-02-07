<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "Swagger",
  components: { iFrame },
  data() {
    return {
      // 原swagger文档位置为/swagger-ui/index.html，这里使用knife4j增强文档位置/doc.html
      url: process.env.VUE_APP_BASE_API + "/doc.html"
    };
  },
};
</script>
