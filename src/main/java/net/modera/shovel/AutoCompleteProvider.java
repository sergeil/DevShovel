package net.modera.shovel;

import java.util.List;

public interface AutoCompleteProvider {

	List<String> getAutoCompleteValues(String text);

}
