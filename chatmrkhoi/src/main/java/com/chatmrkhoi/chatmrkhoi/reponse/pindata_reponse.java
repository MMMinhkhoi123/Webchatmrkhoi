package com.chatmrkhoi.chatmrkhoi.reponse;

import java.util.List;

import com.chatmrkhoi.chatmrkhoi.request.addpin_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class pindata_reponse {
	mess_reponse mess_reponse;
	List<pin_reponse> pin_reponses;
	pin_reponse pin_chid;
}
