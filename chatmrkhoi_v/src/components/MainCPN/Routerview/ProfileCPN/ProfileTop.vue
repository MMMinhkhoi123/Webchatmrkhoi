<script setup>
  import store_everyone from "../../../../handle/Everyone/index";
  import data from "../../../../handle/Screen_chat/index";
  import other from "../../../../handle/Common/index";
  import imgCPN from "../../../NewCPN/imgtake.vue";
  import { useRouter } from "vue-router";
  const { data_profile} = data();
  const { authen, url_img } = other();
  const { check_friend, check_request, addfriend, destroy_agree, check_send_request, agree, showalert, destroy } = store_everyone();
  const router = useRouter();
  const select_chat = (item) => {
    localStorage.setItem("data-select", item.coderoom)
    router.push({ name: "chat", query: { id: item.id }})
}
</script>
<template>
    <div class="action__top">

        <!-- avata -->
        <div class="action__top--img" @click="$router.push({ query: {  action: 'my', codeimg: authen.images, type: authen.type_img  } })"  v-if="$route.query.id == null">
            <img class="top__img" v-if="authen.type_img != 'rs'" :src="url_img + authen.images">
            <imgCPN class="top__img" v-if="authen.type_img == 'rs'" :name="authen.images" />
        </div>

        <div class="action__top--img" @click="$router.push({ query: {  action: 'my', codeimg: data_profile($route.query.id).images, type: data_profile($route.query.id).type_img  } })"  v-if="$route.query.id != null">
            <img class="top__img" v-if="data_profile($route.query.id).type_img != 'rs'" :src="url_img + data_profile($route.query.id).images">
            <img class="top__img" v-if="data_profile($route.query.id).type_img == 'rs'" loading="lazy" :src="'/src/assets/images/avata_org/' + data_profile($route.query.id).images">
        </div>

        <div class="action__top--introduce">
            <!-- name -->
            <h1 class="top__name" v-if="$route.query.id == null">{{ authen.fullname }}</h1>
            <h1 class="top__name" v-if="$route.query.id != null">{{ data_profile($route.query.id).fullname }}</h1>

            <!-- count friend -->
            <div class="top__friend">
                <strong class="top__friend--content" >
                    <img class="friend__icon" src="../../../../assets/icon/check.png" alt="">   
                    <span  class="friend__text"  v-if="$route.query.id == null">{{ $store.state.everyone.array_friend.length }}</span>
                    <span  class="friend__text" v-if="$route.query.id != null">{{ data_profile($route.query.id).countfriend  }}</span>
                </strong>
            </div>

            <!-- action-friend -->
            <div  class="top__setting" v-if="$route.query.action != 'my' && $route.query.id != null">
                <button class="top__setting--submit" @click="destroy(data_profile($route.query.id).id)"  
                v-if="check_send_request(data_profile($route.query.id).id) == true && data_profile($route.query.id).status != 'not' && check_friend(data_profile($route.query.id).id) == false" 
                style="background: var(--color4);">
                {{ $t('TextMain.Profile.Screen.Option.DestroyRequest') }}
                </button>
                <button class="top__setting--submit" @click="agree(data_profile($route.query.id).id)" 
                v-if="check_request(data_profile($route.query.id).id) == true && check_friend(data_profile($route.query.id).id) == false" style="background: var(--color4);">
                {{ $t('TextMain.Profile.Screen.Option.Agree') }}
                </button>
                <button class="top__setting--submit" @click="destroy_agree(data_profile($route.query.id).id)"  
                v-if="check_request(data_profile($route.query.id).id) == true && check_friend(data_profile($route.query.id).id) == false" style="background: var(--color4);">
                {{ $t('TextMain.Profile.Screen.Option.DisAgree') }}
                </button>
                <button class="top__setting--submit" @click="addfriend(data_profile($route.query.id).id)"  
                v-if="data_profile($route.query.id).status == 'not'"  style="background: var(--color3);">
                {{ $t('TextMain.Profile.Screen.Option.MakeFriend') }}
                </button>
                <button class="top__setting--submit pre" 
                v-if="check_friend(data_profile($route.query.id).id) == true" @click="showalert(data_profile($route.query.id))">
                {{ $t('TextMain.Profile.Screen.Option.Unfriend') }}
                </button>
                <button class="top__setting--submit send" 
                v-if="check_friend(data_profile($route.query.id).id) == true" @click="select_chat(data_profile($route.query.id))">
                    <font-awesome-icon :icon="['fas', 'paper-plane']" />
                  {{ $t('TextMain.Profile.Screen.Option.SendMess') }}
                </button>
            </div>

        </div>
</div>
</template>
<style scoped>
.action__top {
    display: flex;
    padding: 10px;
    align-items: center;
    background: var(--color1);
}
.action__top--img {
    padding: 32px 22px;
    display: flex;
    gap: 15px;
    flex-direction: column;
    align-items: center;
}
.top__img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
    background: white;
    box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
}

.action__top--introduce {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.top__name {
    font-style: normal;
    font-weight: 700;
    line-height: normal;
    font-size: 20px;
    margin: 0;
    color: white;
} 
.top__friend {
    display: flex;
    gap: 14px;
}
.top__friend--content {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
    color: white;
    font-size: 18px;
}
.friend__text {
    color: white;
    font-style: normal;
    font-weight: 500;
    line-height: normal;
}
.friend__icon{
    width: 20px;
    height: 20px;
}
.top__setting {
    display: flex;
    gap: 10px;
} 
.top__setting--submit {
    padding: 10px 20px;
    border: none;
    color: white;
    cursor: pointer;
    outline: none;
}
.pre {
    background: red !important;
}
.send {
    color: black;
}
</style>