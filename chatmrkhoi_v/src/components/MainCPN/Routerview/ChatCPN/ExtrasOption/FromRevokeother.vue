<template>
    <div @click="$store.state.avaible_chat.unmess.status = null" class="bgwhite">
    </div>
    <div  class="fromrevoke">
        <h3>On whose side do you want to remove this message?</h3>
        <div class="optio_none">
            <label class="chose">
                <input id="one" v-model="datachonse" type="radio" name="abc" />
                <span></span>
                <strong>Recall with everyone</strong>
            </label>
            <p>This message will be revoked to everyone in the chat. Other people may have 
                seen or forwarded the message. Recalled messages can still be reported.</p>
        </div>
        <div class="optio_two">
            <label class="chose">
                <input id="two" type="radio" name="abc"/>
                <span></span>
                <strong>Remove on your side</strong>
            </label>
            <p>We'll remove this message on your side. Other people in the chat can still see it.</p>
        </div>
        <div class="option">
            <button @click="$store.state.avaible_chat.unmess.status = null">Cancel</button>
            <button @click="unmesschose">Delete, remove</button>
        </div>
    </div>
</template>
<script>
import { ref } from 'vue'
import store from '../../../../../store/index'
import socket from "../../../../../socket/common"
import connect from "../../../../../socket/handle_chat_private"
import data from "../../../../../handle/Screen_chat/index";
export default {
    updated() {
        this.room = this.$route.query.rom;
    },
    created() {
        this.room = this.$route.query.rom;
    },
    setup() {
        const { stompClient } = socket();
        const { authen } = data();
        const room = ref(null);
        const unmesschose = () => {
            const div = document.getElementById("one");
            const div2 = document.getElementById("two");
            var type = 'one'
            if (div2.checked == false) {
                type = 'all'
            }
            if (div.checked ==  false && div2.checked == false) {
                alert("not valid")
            } else {
                    const datapost = {
                    type: type,
                    idmess: store.state.avaible_chat.unmess.idmess,
               }
            store.dispatch("chat/unmess", datapost).then(() => {
                if(type == 'all') {
                    connect({ revoke: authen.value.id, mess: store.state.chat.data_after_update }, localStorage.getItem("data-select"))
                }
            })
            }
            store.state.avaible_chat.unmess.status = null
            
        }

        const datachonse = ref(2);
        return {
            room,
          unmesschose,
          datachonse,
        }
    },
}
</script>
<style scoped>
.optio_none , .optio_two {
    text-align: start;
    margin-bottom: 20px;
    margin-left: 20px;
}
p {
    padding: 10px;
    margin: 0;
}
.chose {
    display: flex;
    gap: 10px;
    align-items: center;
    cursor: pointer;
}
.chose > input {
    display: none;
}
.chose >  span {
    display: block;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    border: 2px solid rgb(207, 202, 202);
    transition: 0.3s;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
}
.chose > input:checked ~ span{
    border: 2px solid rgb(255, 255, 255);
    background: var(--color1);
}
h3 {
    margin: 0;
    margin-bottom: 20px;
    padding: 20px;
    border-radius: 10px 10px 0 0 ;
}
  .bgwhite {
    position: fixed;
    inset: 0;
    background: rgba(255, 255, 255, 0.612);
    z-index: 100;
    filter: blur(20px);
  }
  .fromrevoke {
    width: 700px;
    background: white;
    border-radius: 10px;
    text-align: center;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 111;
    box-shadow: 0 0 19px rgba(0, 0, 0, 0.253);
  }
  .option {
    padding: 20px;
    display: flex;
    gap: 10px;
    justify-content: space-between;
  }
  .option > button {
    border: none;
    color: rgb(0, 0, 0);
    width: 300px;
    height: 50px;
    transition: 0.3s;
  }
  .option > button:nth-child(1):hover {
    background: var(--color2);
    color: white;
  }
  .option > button:nth-child(2) {
    background: #ffffff;
    font-size: 15px;
    color: var(--color1);
    border: 1px solid var(--color1);
  }
  .option > button:nth-child(2):hover {
    background: var(--color1);
    color: white;
  }
</style>