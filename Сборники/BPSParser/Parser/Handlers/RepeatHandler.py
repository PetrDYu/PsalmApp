import re

from Parser import cur_psalm
from Parser.Elements.PsalmString import RepeatablePsalmString, PlainString, PsalmString
from Parser.Handlers.BaseHandler import BaseHandler
from Parser.Handlers.PsalmStringData import PsalmStringData
from Parser.Configs import REPEAT_3_DBP_RE, REPEAT_WITH_INNER_AND_ANY_OUTER, REPEAT_RE


class RepeatHandler(BaseHandler):
    current_id = 0
    opened_repeat_id_list = []

    @classmethod
    def reset_current_id(cls):
        cls.current_id = 0

    @classmethod
    def return_current_id_and_increment(cls):
        temp_id = cls.current_id
        cls.current_id += 1
        return temp_id

    @classmethod
    def add_opened_id(cls, string_id):
        cls.opened_repeat_id_list.append(string_id)

    @classmethod
    def return_and_remove_last_id(cls):
        return cls.opened_repeat_id_list.pop()

    def __init__(self, next_handler: BaseHandler = None):
        super().__init__(next_handler)

    def handle_psalm_string(self, s: PsalmStringData):
        handling_data = s.str_data

        children_3_dp, left_border, full_string_handled, handling_data = self.handle_3_dp(handling_data)

        if full_string_handled:
            self.append_to_psalm(children_3_dp)
            return

        children, full_string_handled = self.handle_inner_with_outer(handling_data, children_3_dp, left_border)

        if full_string_handled:
            self.append_to_psalm(children)
            return

        children, full_string_handled = self.handle_simple_repeat(handling_data, children_3_dp, left_border)

        if full_string_handled:
            self.append_to_psalm(children)
            return

        self.is_success = False

    @staticmethod
    def append_to_psalm(children: list):
        psalm_string = PsalmString()
        psalm_string.append_children(children)
        cur_psalm.parts[-1].append_strings(psalm_string)

    @staticmethod
    def handle_3_dp(handling_data):
        r = re.search(REPEAT_3_DBP_RE, handling_data)
        left_border = -1
        children = []
        full_string_handled = False
        if r:
            repeat_3 = RepeatablePsalmString(RepeatHandler.return_current_id_and_increment(), 3, True, True)
            children.append(repeat_3)
            repeat_3.append_child(PlainString(r.group('text')))
            left_border = r.start()
            right_border = r.end()
            handling_data = handling_data[0:left_border] + handling_data[right_border:]
            if handling_data.find(':') == -1:
                full_string_handled = True
                if left_border != 0:
                    children.insert(0, PlainString(handling_data[: left_border]))
                if left_border != len(handling_data) - 1:
                    children.append(PlainString(handling_data[left_border:]))
        return children, left_border, full_string_handled, handling_data

    @staticmethod
    def handle_inner_with_outer(handling_data: str, children_3_dp: list, left_border: int):
        full_string_handled = False
        children = []
        r = re.match(REPEAT_WITH_INNER_AND_ANY_OUTER, handling_data)
        if r:
            full_string_handled = True
            n_inner = int(r.group("n_inner") if r.group("n_inner") is not None else 2)
            n_outer = int(r.group("n_outer") if r.group("n_outer") is not None else 2)

            inner_repeat = RepeatablePsalmString(
                RepeatHandler.return_current_id_and_increment(),
                repetition_rate=n_inner,
                left_closed=True,
                right_closed=True
            )
            inner_repeat.append_children(
                RepeatHandler.get_children_with_3_dp(
                    left_border,
                    children_3_dp,
                    r,
                    "text_inner"
                )
            )
            # TODO возможно, здесь должно было быть and, а не or
            if r.group("dbp_outer_left") or r.group("dbp_outer_right"):
                outer_repeat_id = 0
                if r.group("dbp_outer_left"):
                    outer_repeat_id = RepeatHandler.return_current_id_and_increment()
                    RepeatHandler.add_opened_id(outer_repeat_id)
                if r.group("dbp_outer_right"):
                    outer_repeat_id = RepeatHandler.return_and_remove_last_id()

                outer_repeat = RepeatablePsalmString(
                    string_id=outer_repeat_id,
                    repetition_rate=n_outer,
                    right_closed=r.group("dbp_outer_right") is not None,
                    left_closed=r.group("dbp_outer_left") is not None
                )
                if r.group("text_outer_left"):
                    outer_repeat.append_children(
                        RepeatHandler.get_children_with_3_dp(
                            left_border,
                            children_3_dp,
                            r,
                            "text_outer_left"
                        )
                    )

                outer_repeat.append_child(inner_repeat)

                if r.group("text_outer_right"):
                    outer_repeat.append_children(
                        RepeatHandler.get_children_with_3_dp(
                            left_border,
                            children_3_dp,
                            r,
                            "text_outer_right"
                        )
                    )

                children.append(outer_repeat)
            else:
                if r.group("text_outer_left"):
                    children.extend(
                        RepeatHandler.get_children_with_3_dp(
                            left_border,
                            children_3_dp,
                            r,
                            "text_outer_left"
                        )
                    )

                children.append(inner_repeat)

                if r.group("text_outer_right"):
                    children.extend(
                        RepeatHandler.get_children_with_3_dp(
                            left_border,
                            children_3_dp,
                            r,
                            "text_outer_right"
                        )
                    )
        return children, full_string_handled

    @staticmethod
    def handle_simple_repeat(handling_data: str, children_3_dp: list, left_border: int):
        r = re.match(REPEAT_RE, handling_data)
        full_string_handled = False
        children = []
        if r and (r.group("dbp_left") or r.group("dbp_right")):
            full_string_handled = True
            n = int(r.group("n") if r.group("n") is not None else 2)
            repeat_id = 0
            if r.group("dbp_left"):
                repeat_id = RepeatHandler.return_current_id_and_increment()
                RepeatHandler.add_opened_id(repeat_id)
            if r.group("dbp_right"):
                repeat_id = RepeatHandler.return_and_remove_last_id()
            repeat = RepeatablePsalmString(
                repeat_id,
                repetition_rate=n,
                right_closed=r.group("dbp_right") is not None,
                left_closed=r.group("dbp_left") is not None
            )
            repeat.append_children(RepeatHandler.get_children_with_3_dp(
                            left_border,
                            children_3_dp,
                            r,
                            "text"
                        )
            )
            children.append(repeat)

        return children, full_string_handled

    @staticmethod
    def get_children_with_3_dp(border: int, children_3_dp: list, r: re.Match, group_name: str):
        group_text = r.group(group_name)
        if not RepeatHandler.border_in_group(border, r, group_name):
            return [PlainString(group_text)]

        local_border = border - r.start(group_name)
        children = []
        if group_text[:local_border].strip():
            children.append(PlainString(group_text[:local_border]))

        children.extend(children_3_dp)

        if group_text[local_border:].strip():
            children.append(PlainString(group_text[local_border:]))
        return children

    @staticmethod
    def border_in_group(border: int, r: re.Match, group_name: str):
        return r.start(group_name) <= border <= r.end(group_name)
